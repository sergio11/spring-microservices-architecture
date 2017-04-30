package sanchez.sergio.messages.admin;

/**
 * Enum with Visitor pattern
 * @author sergio
 */
public enum UsersManadgementMessageType {
    
    USER_CONNECTED {
        @Override
        public void accept(UsersManadgementVisitor visitor, Long idUser) {
            visitor.visitUserConnected(idUser);
        }
    },
    USER_DISCONNECTED {
        @Override
        public void accept(UsersManadgementVisitor visitor, Long idUser) {
            visitor.visitUserDisconnected(idUser);
        }
    };
    
    public abstract void accept( UsersManadgementVisitor visitor, Long idUser );

    public interface UsersManadgementVisitor {
        void visitUserConnected(Long idUser);
        void visitUserDisconnected(Long idUser);
    }
}
