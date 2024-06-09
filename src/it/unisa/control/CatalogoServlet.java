package it.unisa.control;

import java.io.IOException; 
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.ProdottoBean;
import it.unisa.model.ProdottoDao;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private boolean isValidInput(String input) {
        // Pattern per validare l'input (puoi modificare questo pattern secondo le tue esigenze)
        String regex = "^[a-zA-Z0-9 ]*$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input).matches();
    }

	private String sanitizeInput(String input) {
        // Sanifica i caratteri speciali sostituendoli con le loro entità HTML
        if (input != null) {
            input = input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }
        return input;
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ProdottoDao prodDao = new ProdottoDao();
		ProdottoBean bean = new ProdottoBean();
		String sort = request.getParameter("sort");
		String action = request.getParameter("action");
		String redirectedPage = request.getParameter("page");;
	
		try {
		    if(action != null) {
		        if(action.equalsIgnoreCase("add")) {
		            String nome = request.getParameter("nome");
		            if (!this.isValidInput(nome)) {
		                throw new IllegalArgumentException("Invalid input for nome");
		            }
		            bean.setNome(this.sanitizeInput(nome));
		            bean.setDescrizione(this.sanitizeInput(request.getParameter("descrizione")));
		            bean.setIva(request.getParameter("iva"));
		            bean.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
		            bean.setQuantita(Integer.parseInt(request.getParameter("quantit�")));
		            bean.setPiattaforma(request.getParameter("piattaforma"));
		            bean.setGenere(request.getParameter("genere"));
		            bean.setImmagine(request.getParameter("img"));
		            bean.setDataUscita(request.getParameter("dataUscita"));
		            bean.setDescrizioneDettagliata(this.sanitizeInput(request.getParameter("descDett")));
		            bean.setInVendita(true);
		            prodDao.doSave(bean);
		        } else if(action.equalsIgnoreCase("modifica")) {
		            bean.setIdProdotto(Integer.parseInt(request.getParameter("id")));
		            String nome = request.getParameter("nome");
		            if (!this.isValidInput(nome)) {
		                throw new IllegalArgumentException("Invalid input for nome");
		            }
		            bean.setNome(this.sanitizeInput(nome));
		            bean.setDescrizione(this.sanitizeInput(request.getParameter("descrizione")));
		            bean.setIva(request.getParameter("iva"));
		            bean.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
		            bean.setQuantita(Integer.parseInt(request.getParameter("quantit�")));
		            bean.setPiattaforma(request.getParameter("piattaforma"));
		            bean.setGenere(request.getParameter("genere"));
		            bean.setImmagine(request.getParameter("img"));
		            bean.setDataUscita(request.getParameter("dataUscita"));
		            bean.setDescrizioneDettagliata(this.sanitizeInput(request.getParameter("descDett")));
		            bean.setInVendita(true);
		            prodDao.doUpdate(bean); 
		        }

		        request.getSession().removeAttribute("categorie");
		    }
		} catch (SQLException e) {
		    System.out.println("Error:" + e.getMessage());
		} catch (IllegalArgumentException e) {
		    System.out.println("Error:" + e.getMessage());
		}

		try {
		    request.getSession().removeAttribute("products");
		    request.getSession().setAttribute("products", prodDao.doRetrieveAll(sort));
		} catch (SQLException e) {
		    System.out.println("Error:" + e.getMessage());
		}

		response.sendRedirect(request.getContextPath() + "/" + redirectedPage);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
