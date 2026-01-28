import api from './api';

const reviewApi = {
    addReview: (reviewData) => api.post('/reviews', reviewData),
    getCourseReviews: (courseId) => api.get(`/reviews/course/${courseId}`),
};

export default reviewApi;
