package textproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class BookReaderApplication {

    public static void main(String[] args) throws FileNotFoundException {
        // Skapa en Scanner för att läsa texten från "nilsholg.txt" med UTF-8-teckenkodning
        Scanner textScanner = new Scanner(new File("nilsholg.txt"), "UTF-8");
        // Här hanterar vi speciella tecken i början av filen, om de finns
        textScanner.findWithinHorizon("\uFEFF", 1);
        // Dela upp texten i ord med hjälp av reguljära uttryck (regex)
        textScanner.useDelimiter("(\\s|,|\\.|:|;|!|\\?|'|\\\")+");

        // Skapa en Scanner för att läsa ord från "undantagsord.txt" med UTF-8-teckenkodning
        Scanner stopWordsScanner = new Scanner(new File("undantagsord.txt"), "UTF-8");
        stopWordsScanner.useDelimiter(" ");

        // Skapa en mängd (Set) för att lagra undantagsord
        Set<String> stopWords = new HashSet<>();

        // Läs undantagsord från filen och lägg till dem i mängden
        while (stopWordsScanner.hasNext()) {
            String stopWord = stopWordsScanner.next().toLowerCase();
            stopWords.add(stopWord);
        }

        // Skapa en GeneralWordCounter med undantagsordsmängden
        GeneralWordCounter wordCounter = new GeneralWordCounter(stopWords);

        // Läs igenom texten och behandla varje ord
        while (textScanner.hasNext()) {
            String word = textScanner.next().toLowerCase();
            wordCounter.process(word);
        }

        // Stäng scannrarna
        textScanner.close();
        stopWordsScanner.close();

        // Skapa en instans av BookReaderController med wordCounter som argument
        BookReaderController controller = new BookReaderController(wordCounter);
    }
}