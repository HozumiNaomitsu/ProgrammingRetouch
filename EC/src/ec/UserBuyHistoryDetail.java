package ec;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.BuyDataBeans;
import beans.ItemDataBeans;
import dao.BuyDAO;
import dao.BuyDetailDAO;

/**
 * 購入履歴画面
 * @author d-yamaguchi
 *
 */
@WebServlet("/UserBuyHistoryDetail")
public class UserBuyHistoryDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		//try・catchを使う理由はこの中でエラーが出たときcatchで受け取ってエラーページにリダイレクトする為。
		try {
			//①String id = request.getParameter("id");
			//②BuyDataBeans bdb = BuyDAO.getBuyDataBeansByBuyId(Integer.parseInt(id));とも書ける。
			//↑②staticが付いているのでこう書く。
			int id = Integer.parseInt(request.getParameter("id"));

			//上の購入時、配送方法、合計金額
			BuyDataBeans bdb = BuyDAO.getBuyDataBeansByBuyId(id);
			request.setAttribute("bdb", bdb);

			//下の商品名、単価
			ArrayList<ItemDataBeans> itemDataBeansList = BuyDetailDAO.getItemDataBeansListByBuyId(bdb.getId());
            request.setAttribute("itemDBList",itemDataBeansList );

            //下の商品名(配送名)、単価(配送金額)
//            DeliveryMethodDataBeans dmdb= DeliveryMethodDAO. getDeliveryMethodDataBeansByID(bdb.getDelivertMethodId());
//            request.setAttribute("dmdb",dmdb);

			request.getRequestDispatcher(EcHelper.USER_BUY_HISTORY_DETAIL_PAGE).forward(request, response);

		} catch (Exception e) {

		}
	}
}
