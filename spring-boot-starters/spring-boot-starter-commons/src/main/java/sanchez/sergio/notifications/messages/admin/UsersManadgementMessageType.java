package sanchez.sergio.notifications.messages.admin;

/**
 * Enum with Visitor pattern
 * @author sergio
 */
public enum UsersManadgementMessageType {
    
    USER_CONNECTED {
        @Override
        public void accept(UsersManadgementVisitor visitor, String username) {
            visitor.visitUserConnected(username);
        }
    },
    USER_DISCONNECTED {
        @Override
        public void accept(UsersManadgementVisitor visitor, String username) {
            visitor.visitUserDisconnected(username);
        }
    };
    
    public abstract void accept( UsersManadgementVisitor visitor, String username );

    public interface UsersManadgementVisitor {
        void visitUserConnected(String username);
        void visitUserDisconnected(String username);
    }
}
