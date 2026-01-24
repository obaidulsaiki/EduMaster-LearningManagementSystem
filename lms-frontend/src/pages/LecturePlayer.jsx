import React, { useEffect, useRef, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { completeLecture } from "../api/courseProgressApi";
import api from "../api/api";
import "./LecturePlayer.css";

/* ======================
   SAFE YOUTUBE ID PARSER
====================== */
const extractYoutubeId = (url) => {
  if (!url) return null;
  if (!url.includes("http")) return url;
  if (url.includes("v=")) return url.split("v=")[1].split("&")[0];
  if (url.includes("youtu.be/")) return url.split("youtu.be/")[1];
  return null;
};

const LecturePlayer = () => {
  const { courseId, lectureId } = useParams();
  const navigate = useNavigate();

  const playerRef = useRef(null);
  const completedOnce = useRef(false);

  const [videoId, setVideoId] = useState(null);
  const [videoEnded, setVideoEnded] = useState(false);
  const [nextLectureId, setNextLectureId] = useState(null);

  /* ======================
     LOAD COURSE & LECTURE
  ====================== */
  useEffect(() => {
    const loadLecture = async () => {
      const res = await api.get(`/courses/${courseId}`);

      const lectures = [...(res.data.lectures || [])].sort(
        (a, b) => (a.orderIndex ?? 0) - (b.orderIndex ?? 0),
      );

      const index = lectures.findIndex(
        (l) => String(l.id) === String(lectureId),
      );

      if (index === -1) return;

      // 🔥 RESET STATE ON LECTURE CHANGE
      completedOnce.current = false;
      setVideoEnded(false);

      setVideoId(extractYoutubeId(lectures[index].videoUrl));
      setNextLectureId(lectures[index + 1]?.id || null);
    };

    loadLecture();
  }, [courseId, lectureId]);

  /* ======================
     YOUTUBE PLAYER LIFECYCLE
  ====================== */
  useEffect(() => {
    if (!videoId) return;

    // 🔥 DESTROY OLD PLAYER
    if (playerRef.current) {
      playerRef.current.destroy();
      playerRef.current = null;
    }

    const createPlayer = () => {
      playerRef.current = new window.YT.Player("yt-player", {
        width: "100%",
        height: "100%",
        videoId,
        playerVars: {
          rel: 0,
          modestbranding: 1,
          controls: 1,
        },
        events: {
          onStateChange: (e) => {
            if (
              e.data === window.YT.PlayerState.ENDED &&
              !completedOnce.current
            ) {
              completedOnce.current = true;
              setVideoEnded(true);
            }
          },
        },
      });
    };

    if (!window.YT || !window.YT.Player) {
      const tag = document.createElement("script");
      tag.src = "https://www.youtube.com/iframe_api";
      window.onYouTubeIframeAPIReady = createPlayer;
      document.body.appendChild(tag);
    } else {
      createPlayer();
    }

    // 🔥 CLEANUP ON UNMOUNT
    return () => {
      if (playerRef.current) {
        playerRef.current.destroy();
        playerRef.current = null;
      }
    };
  }, [videoId]);

  /* ======================
     COMPLETE & NAVIGATE
  ====================== */
  const handleNext = async () => {
    await completeLecture(lectureId);

    if (nextLectureId) {
      navigate(`/course/${courseId}/lecture/${nextLectureId}`);
    } else {
      navigate(`/course/${courseId}`);
    }
  };

  return (
    <div className="lecture-player-page">
      {/* VIDEO */}
      <div className="video-wrapper">
        <div id="yt-player" />
      </div>

      {/* CONTROLS */}
      <div className="lecture-controls">
        <button onClick={() => navigate(`/course/${courseId}`)}>← Back</button>

        {!videoEnded ? (
          <button disabled>✔ Complete</button>
        ) : nextLectureId ? (
          <button onClick={handleNext}>▶ Next Lecture</button>
        ) : (
          <button onClick={handleNext}>✔ Finish Course</button>
        )}
      </div>
    </div>
  );
};

export default LecturePlayer;
