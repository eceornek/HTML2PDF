import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.lowagie.text.pdf.BaseFont;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.PDFRenderer;
import org.xhtmlrenderer.simple.ImageRenderer;

import javax.swing.text.html.HTML;

public class HTMLToPDF {

    private static final String GORKEM_LOCAL_PATH= "/Users/gorkemkaraduman/Coding/IBM-Code/HTML2PDF/";
    private static final String ECE_LOCAL_PATH= "/Users/User/Desktop/IBM/HTMLToPDF/";


    public static void ConvertHTMLToPDF(String input_HTML_path, String output_pdf_path){
        AdjustLayoutFixFont(input_HTML_path);
        try {
            String XHTML = HTMLToXHTML(input_HTML_path);
            XHTMLToPDF(XHTML, output_pdf_path);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void AdjustLayoutFixFont(String input_HTML_path){
        File input = new File(input_HTML_path);
        Document doc = null;
        try {
            doc = Jsoup.parse(input, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements css = doc.select("style");
        css.prepend("* { font-family: 'Times New Roman';}");
        doc.select("table[width=800]").attr("width", "100%");
        doc.select("#notesTable").attr("width", "100%") ; // a with href
        doc.select("#notesTableTd").attr("height", "30");
        doc.select("script").remove(); // remove Javascript
        try { // Write to file
            FileUtils.writeStringToFile(input, doc.outerHtml(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String HTMLToXHTML(String html) throws IOException {
        String inputHTML = new String(Files.readAllBytes(Paths.get(html)));
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setXHTML(true);
        tidy.setShowWarnings(false);
        tidy.setMakeClean(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputHTML.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString("UTF-8");
    }

    private static void XHTMLToPDF(String xhtml, String outFileName) throws IOException {
        File output = new File(outFileName);
        ITextRenderer renderer = new ITextRenderer();
        SharedContext sharedContext = renderer.getSharedContext();
        sharedContext.setPrint(true);
        sharedContext.setInteractive(false);
        sharedContext.setReplacedElementFactory(new B64ImgReplacedElementFactory());
        sharedContext.getTextRenderer().setSmoothingThreshold(0);
        String fontpath = "fonts/arial-unicode-ms.ttf".replace("/", File.separator);
        String fontpath_tnr = "fonts/times-new-roman.ttf".replace("/", File.separator);
        try {
            renderer.getFontResolver().addFont(fontpath_tnr, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.getFontResolver().addFont(ECE_LOCAL_PATH + "Butterfly.ttf", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String baseUrl = FileSystems.getDefault()
                    .getPath(ECE_LOCAL_PATH)
                    .toUri()
                    .toURL()
                    .toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        renderer.setDocumentFromString(xhtml);
        renderer.layout();
        try (OutputStream os = new FileOutputStream(output)) {
            renderer.createPDF(os);
            System.out.println("PDF creation completed");
        }
    }
}