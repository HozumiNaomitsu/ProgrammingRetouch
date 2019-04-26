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
 * 検索結果画面
 *
 * @author d-yamaguchi
 *
 */
@WebServlet("/ItemSearchResult")
public class ItemSearchResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//1ページに表示する商品数
	final static int PAGE_MAX_ITEM_COUNT = 8;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//try・catchを使う理由はこの中でエラーが出たときcatchで受け取ってエラーページにリダイレクトする為。
		try {

			 /*Enter押して表示なのでpostParameter。getParameter,postParameterはrequest.getParameterで取る。
			  *request.getParameter("search_word")のsearch_wordはitemsearchresult.jspのnameである。
			  */
			String searchWord = request.getParameter("search_word");

//			int pageNum;
//			if(Integer.parseInt(request.getParameter("page_num") == null) {
//				pageNum = 1;
//			}else {
//				pageNum = request.getParameter("page_num");
//			}
			//表示ページ番号 未指定の場合 1ページ目を表示
			int pageNum = Integer.parseInt(request.getParameter("page_num") == null ? "1" : request.getParameter("page_num"));

			// 新たに検索されたキーワードをセッションに格納する
			session.setAttribute("searchWord", searchWord);

			// 商品リストを取得 ページ表示分のみ
			ArrayList<ItemDataBeans> searchResultItemList = ItemDAO.getItemsByItemName(searchWord, pageNum, PAGE_MAX_ITEM_COUNT);

			// 検索ワードに対しての総ページ数を取得
			double itemCount = ItemDAO.getItemCount(searchWord);
			int pageMax = (int) Math.ceil(itemCount / PAGE_MAX_ITEM_COUNT);

			//総アイテム数
			request.setAttribute("itemCount", (int) itemCount);
			// 総ページ数
			request.setAttribute("pageMax", pageMax);
			// 表示ページ
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("itemList", searchResultItemList);

			request.getRequestDispatcher(EcHelper.SEARCH_RESULT_PAGE).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			//try・catch内でエラーが出たときここに来る。
			session.setAttribute("errorMessage", e.toString());
			response.sendRedirect("Error");
		}
	}

}
