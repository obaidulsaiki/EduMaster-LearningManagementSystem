import API from "./api";

export const toggleWishlist = (courseId) => API.post(`/wishlist/toggle/${courseId}`);

export const getMyWishlist = () => API.get("/wishlist");

export const checkWishlistStatus = (courseId) => API.get(`/wishlist/check/${courseId}`);
