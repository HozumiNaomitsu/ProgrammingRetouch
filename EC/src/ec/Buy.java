package ec;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DeliveryMethodDataBeans;
import beans.ItemDataBeans;
import dao.DeliveryMethodDAO;

/**
 * 商品購入画面
 *
 * @author d-yamaguchi
 *
 */
@WebServlet("/Buy")
public class Buy extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッション
		HttpSession session = request.getSession();
		//try・catchを使う理由はこの中でエラーが出たときcatchで受け取ってエラーページにリダイレクトする為。
		try {

			Boolean isLogin = session.getAttribute("isLogin") != null ? (Boolean) session.getAttribute("isLogin") : false;
			ArrayList<ItemDataBeans> cart = (ArrayList<ItemDataBeans>) session.getAttribute("cart");

			if (!isLogin) {
				// Sessionにリターンページ情報を書き込む
				session.setAttribute("returnStrUrl", "Buy");
				// Login画面にリダイレクト
				response.sendRedirect("Login");

			} else if (cart.size() == 0) {
				request.setAttribute("cartActionMessage", "購入する商品がありません");
				request.getRequestDispatcher(EcHelper.CART_PAGE).forward(request, response);
			} else {
				// 配送方法をDBから取得
				ArrayList<DeliveryMethodDataBeans> dMDBList = DeliveryMethodDAO.getAllDeliveryMethodDataBeans();
				request.setAttribute("dmdbList", dMDBList);
				request.getRequestDispatcher(EcHelper.BUY_PAGE).forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//try・catch内でエラーが出たときここに来る。
			session.setAttribute("errorMessage", e.toString());
			response.sendRedirect("Error");
		}
	}
}
