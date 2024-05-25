public class CommentHandler {
    private CommentDAO commentDAO;

    public CommentHandler(EntityManager entityManager) {
        this.commentDAO = new CommentDAO(entityManager);
    }

    public Comment addCommentToPost(Long postId, String content, String filePath) {
        Post post = entityManager.find(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(content);
        comment.setFilePath(filePath);
        return commentDAO.saveComment(comment);
    }

    public void removeComment(Long commentId) {
        commentDAO.deleteComment(commentId);
    }

    public Comment updateCommentContent(Long commentId, String newContent) {
        Comment comment = entityManager.find(Comment.class, commentId);
        if (comment == null) {
            throw new IllegalArgumentException("Comment not found");
        }
        comment.setContent(newContent);
        return commentDAO.updateComment(comment);
    }

    public List<Comment> getCommentsForPost(Long postId) {
        return commentDAO.getCommentsByPostId(postId);
    }
}
