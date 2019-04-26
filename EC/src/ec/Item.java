package ec;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.ItemDataBeans;
import dao.ItemDAO;

/**
 * 商品画面
 * @author d-yamaguchi
 *
 */
@WebServlet("/Item")
public class Item extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品詳細画面
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//try・catchを使う理由はこの中でエラーが出たときcatchで受け取ってエラーページにリダイレクトする為。
		try {
			//選択された商品のIDを型変換し利用
			int id = Integer.parseInt(request.getParameter("item_id"));
			//戻るページ表示用
			int pageNum = Integer.parseInt(request.getParameter("page_num")==null?"1":request.getParameter("page_num"));
			//対象のアイテム情報を取得
			ItemDataBeans item = ItemDAO.getItemByItemID(id);
			//リクエストパラメーターにセット
			request.setAttribute("item", item);
			request.setAttribute("pageNum", pageNum);



			request.getRequestDispatcher(EcHelper.ITEM_PAGE).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			//try・catch内でエラーが出たときここに来る。
			session.setAttribute("errorMessage", e.toString());
			response.sendRedirect("Error");
		}
	}
}
