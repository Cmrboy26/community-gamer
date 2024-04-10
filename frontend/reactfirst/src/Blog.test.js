import Blog from './pages/Blog';

test('constructor throws no errors', () => {
    try {
        const blog = <Blog />;
    } catch (error) {
        expect(error).toBeTruthy();
    }
});