package server.threads.userrequestthread;

import data.Comment;
import data.Product;
import server.DbUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.LinkedList;

public class GetCommentByIdThread extends AbstractUserRequestThread{
    /**
     * 构造方法
     *
     * @param client 客户端socket
     */
    public GetCommentByIdThread(Socket client) {
        super(client);
    }

    @Override
    public void run() {
        ObjectOutputStream oos;
        ObjectInputStream ois;

        try {
            oos = new ObjectOutputStream(getClient().getOutputStream());
            ois = new ObjectInputStream(getClient().getInputStream());

            Product product = (Product)ois.readObject();
            LinkedList<Comment> commentList = DbUtil.getCommentOf(product.getId());

            for(Comment comment:commentList) {
                oos.writeObject(comment);
            }
            oos.writeObject(new Comment(-1, "", ""));
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
