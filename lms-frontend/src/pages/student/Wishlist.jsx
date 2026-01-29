import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { getMyWishlist, toggleWishlist } from "../../api/wishlistApi";
import { Heart, Trash2, ArrowRight, BookOpen } from "lucide-react";
import "./Wishlist.css";

const Wishlist = () => {
    const navigate = useNavigate();
    const [wishlist, setWishlist] = useState([]);
    const [loading, setLoading] = useState(true);

    const loadWishlist = async () => {
        try {
            const res = await getMyWishlist();
            setWishlist(res.data);
        } catch (err) {
            console.error("Failed to load wishlist", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadWishlist();
    }, []);

    const handleRemove = async (courseId) => {
        try {
            await toggleWishlist(courseId);
            setWishlist(wishlist.filter(item => item.courseId !== courseId));
        } catch (err) {
            console.error("Failed to remove from wishlist", err);
        }
    };

    if (loading) return <div className="page-loading">Loading your wishlist...</div>;

    return (
        <div className="wishlist-page">
            <div className="wishlist-container">
                <div className="wishlist-header">
                    <h1>My Wishlist</h1>
                    <p>Courses you're interested in but haven't started yet.</p>
                </div>

                {wishlist.length === 0 ? (
                    <div className="wishlist-empty">
                        <Heart size={80} color="var(--bg-muted)" />
                        <h2>Your wishlist is empty</h2>
                        <p>Explore our courses and save your favorites here!</p>
                        <Link to="/browse" className="browse-btn">Browse Courses</Link>
                    </div>
                ) : (
                    <div className="wishlist-grid">
                        {wishlist.map((item) => (
                            <div key={item.wishlistId} className="wishlist-card">
                                <span className="card-category">{item.category}</span>
                                <h3>{item.title}</h3>
                                <p className="card-teacher">by {item.teacherName}</p>
                                
                                <div className="card-bottom">
                                    <span className="card-price">${item.price}</span>
                                    <div className="card-actions">
                                        <button 
                                            className="remove-wish-btn" 
                                            onClick={() => handleRemove(item.courseId)}
                                            title="Remove from Wishlist"
                                        >
                                            <Trash2 size={18} />
                                        </button>
                                        <button 
                                            className="view-btn" 
                                            onClick={() => navigate(`/course/${item.courseId}`)}
                                        >
                                            View Course <ArrowRight size={16} />
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Wishlist;
