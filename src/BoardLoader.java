/**
 * A BoardLoader which will load XML board files into the scrabble game, allowing players
 * to create and implement their own custom boards.
 *
 * @author Cole Galway
 * @version 12/5/2025
 */

import java.io.File;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Constructs a new BoardLoader, taking no parameters.
 */
public class BoardLoader {
    public BoardLoader() {
    }

    /**
     * Imports an XML file into usable data for the scrabble game.
     * @param board The GameBoard which will be used for the game.
     * @param fileName The name of the file to be loaded containing the XML data.
     */
    public static void importBoardXML(Board board, String fileName) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            //temp holders
            final int[] row = {0};
            final int[] col = {0};
            final Board.Premium[] type = {Board.Premium.NORMAL};
            final String[] current = {null};

            DefaultHandler handler = new DefaultHandler() {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    current[0] = qName;
                    if (qName.equals("Cell")) {
                        String rowAtr = attributes.getValue("row");
                        String colAtr = attributes.getValue("col");
                        String typeAtr = attributes.getValue("type");

                        if (rowAtr == null || colAtr == null) {
                            return;
                        }
                        row[0] = Integer.parseInt(rowAtr);
                        col[0] = Integer.parseInt(colAtr);

                        if (typeAtr != null) {
                            try {
                                type[0] = Board.Premium.valueOf(typeAtr);
                            } catch (IllegalArgumentException e) {
                                System.err.println("Invalid type");
                                type[0] = Board.Premium.NORMAL;
                            }
                        } else {
                            type[0] = Board.Premium.NORMAL;
                        }
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equals("Cell")) {
                        board.setPremium(row[0], col[0], type[0]);
                    }
                    current[0] = "";
                }
            };
            parser.parse(new File(fileName), handler);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error importing Board XML");
            board.setDefaultBoard();
        }

    }
}
