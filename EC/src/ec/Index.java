package ec;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.ItemDataBeans;
import dao.ItemDAO;

/**
 * スタート画面、商品が出るECサイト
 * @author d-yamaguchi
 *
 */
@WebServlet("/Index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//try・catchを使う理由はこの中でエラーが出たときcatchで受け取ってエラーページにリダイレクトする為。
		try {

			//商品情報を取得 getRandItem(4)の数字を変えたら、変えた数字分商品が出てくる。MySQLの数字も同じに変える。
			ArrayList<ItemDataBeans>itemList = ItemDAO.getRandItem(4);

			//リクエストスコープにセット セッションから値を受け取る
			request.setAttribute("itemList", itemList);

			//セッションにsearchWordが入っていたら破棄する
			String searchWord = (String)session.getAttribute("searchWord");
			if(searchWord != null) {
				session.removeAttribute("searchWord");
			}

			request.getRequestDispatcher(EcHelper.TOP_PAGE).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		//try・catch内でエラーが出たときここに来る。
			session.setAttribute("errorMessage", e.toString());
			response.sendRedirect("Error");
		}
	}
}
