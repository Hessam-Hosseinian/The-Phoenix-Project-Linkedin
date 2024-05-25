public class CommentDAO {
    private EntityManager entityManager;

    public CommentDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Comment saveComment(Comment comment) {
        entityManager.getTransaction().begin();
        entityManager.persist(comment);
        entityManager.getTransaction().commit();
        return comment;
    }

    public void deleteComment(Long commentId) {
        entityManager.getTransaction().begin();
        Comment comment = entityManager.find(Comment.class, commentId);
        if (comment != null) {
            entityManager.remove(comment);
        }
        entityManager.getTransaction().commit();
    }

    public Comment updateComment(Comment comment) {
        entityManager.getTransaction().begin();
        Comment updatedComment = entityManager.merge(comment);
        entityManager.getTransaction().commit();
        return updatedComment;
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.post.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }
}
