import { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../context/AuthContext';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const SOCKET_URL = '/ws'; // Backend uses /ws with SockJS

export const useNotifications = () => {
    const { user } = useAuth();
    const [notifications, setNotifications] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0);

    const onMessageReceived = useCallback((msg) => {
        const notification = JSON.parse(msg.body);
        setNotifications(prev => [notification, ...prev]);
        setUnreadCount(prev => prev + 1);

        // Browser notification if supported
        if ("Notification" in window && Notification.permission === "granted") {
            new Notification("EduMaster", { body: notification.message });
        }
    }, []);

    useEffect(() => {
        if (!user) return;

        // Request browser notification permission
        if ("Notification" in window && Notification.permission === "default") {
            Notification.requestPermission();
        }

        const socket = new SockJS('http://localhost:8080/ws');
        const stompClient = Stomp.over(socket);

        // Disable debug logging in production
        stompClient.debug = null;

        stompClient.connect({}, () => {
            const topic = `/topic/notifications/${user.role}-${user.id}`;
            stompClient.subscribe(topic, onMessageReceived);
        }, (err) => {
            console.error("WebSocket connection error:", err);
            // Optional: Implement retry logic
        });

        return () => {
            if (stompClient.connected) {
                stompClient.disconnect();
            }
        };
    }, [user, onMessageReceived]);

    const markAllAsRead = () => {
        setUnreadCount(0);
    };

    return { notifications, unreadCount, markAllAsRead };
};
