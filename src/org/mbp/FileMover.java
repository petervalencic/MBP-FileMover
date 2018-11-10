package org.mbp;

import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class FileMover {

   

    public static void main(String[] args) {
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document doc;
        XPathFactory xpathFactory;

        String temperatura;
        String slanost;

        try {

            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(new URL(args[1]).openStream());
            xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();

            XPathExpression expr = xpath.compile("/root/temp/value/text()");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            temperatura = nodes.item(0).getNodeValue().trim();

            expr = xpath.compile("/root/sal/value/text()");
            nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            slanost = nodes.item(0).getNodeValue().trim();

            PrintWriter writer = new PrintWriter(args[0], "UTF-8");
            writer.println("T: " + temperatura + " °C");
            writer.println("S: " + slanost + " PSU");
            writer.close();

            Path sourceFile = Paths.get(args[0]);
            Path destinationFile = Paths.get(args[1]);
            Files.move(sourceFile, destinationFile, StandardCopyOption.ATOMIC_MOVE);

            /*
            System.out.println("move.." + new Date().toString());
            System.out.println(args[0]);
            System.out.println(args[1]);
            System.out.println("==================");
             */
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Preveri število argumentov");
            System.out.println("arg[0] = naziv začasne datoteke npr. c:/video/temppodatki.txt");
            System.out.println("arg[1] = naziv datoteke iz katere bere FFMPEG npr. c:/video/podatki.txt");
            System.out.println("arg[2] = IP naslov merilne enote od koder se pridobi XML response");
            ex.printStackTrace();
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
