import api from './api';

export const quizApi = {
    getQuizByCourseId: (courseId) => api.get(`/quizzes/course/${courseId}`),
    submitQuiz: (submission) => api.post('/quizzes/submit', submission),
    getUserResult: (courseId) => api.get(`/quizzes/result/${courseId}`),
    manageQuiz: (courseId, quizData) => api.post(`/quizzes/manage/${courseId}`, quizData),
    getQuizForTeacher: (courseId) => api.get(`/quizzes/manage/${courseId}`),
    getResults: (courseId) => api.get(`/quizzes/results/${courseId}`),
};

export default quizApi;
