package ec;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.BuyDataBeans;
import beans.UserDataBeans;
import dao.BuyDAO;
import dao.UserDAO;

/**
 * ユーザー情報画面
 *
 * @author d-yamaguchi
 *
 */
@WebServlet("/UserData")
public class UserData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// セッション開始
		HttpSession session = request.getSession();
		//try・catchを使う理由はこの中でエラーが出たときcatchで受け取ってエラーページにリダイレクトする為。
		try {
			// ログイン時に取得したユーザーIDをセッションから取得
			int userId = (int) session.getAttribute("userId");
			// 更新確認画面から戻ってきた場合Sessionから取得。それ以外はuserIdでユーザーを取得
			UserDataBeans udb = session.getAttribute("returnUDB") == null ? UserDAO.getUserDataBeansByUserId(userId)
					: (UserDataBeans) EcHelper.cutSessionAttribute(session, "returnUDB");
			//↑文はif文と同じ
			//			UserDataBeans udb2;
			//			if( session.getAttribute("returnUDB") == null) {
			//				udb2 = UserDAO.getUserDataBeansByUserId(userId);
			//			} else {
			//				udb2 = (UserDataBeans) EcHelper.cutSessionAttribute(session, "returnUDB");
			//			}

			// 入力された内容に誤りがあったとき等に表示するエラーメッセージを格納する
			String validationMessage = (String) EcHelper.cutSessionAttribute(session, "validationMessage");

			request.setAttribute("validationMessage", validationMessage);
			request.setAttribute("udb", udb);

			// 履歴取得処理

            List<BuyDataBeans> bdbList= BuyDAO.userData(userId);

            request.setAttribute("bdbList",bdbList);

			request.getRequestDispatcher(EcHelper.USER_DATA_PAGE).forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			//try・catch内でエラーが出たときここに来る。
			session.setAttribute("errorMessage", e.toString());
			response.sendRedirect("Error");
		}
	}


}