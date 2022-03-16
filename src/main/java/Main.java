import javax.swing.text.html.HTML;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String invoice1 = "/Users/User/Desktop/IBM/HTMLToPDF/earşiv20210045/invoice1.html";
        String invoice2 = "/Users/User/Desktop/IBM/HTMLToPDF/earşiv20210045/invoice2.html";

        HTMLToPDF.ConvertHTMLToPDF(invoice1, "/Users/User/Desktop/IBM/HTMLToPDF/invoice_1.pdf");
        HTMLToPDF.ConvertHTMLToPDF(invoice2, "/Users/User/Desktop/IBM/HTMLToPDF/invoice_2.pdf");
    }
}
