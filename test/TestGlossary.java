import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import components.map.Map;
import components.map.Map1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Test static methods in Glossary.
 */
public class TestGlossary {

    /**
     * Test compare method under subclass StringLT when o1>o2.
     */
    @Test
    public void testCompare() {
        String o1 = "test";
        String o2 = "apple";
        Glossary.StringLT order = new Glossary.StringLT();

        assertTrue(order.compare(o1, o2) > 0);

    }

    /**
     * Test compare method under subclass StringLT when o1>o2. and o1 and o2
     * have same initial letter.
     */
    @Test
    public void testCompare1() {
        String o1 = "test";
        String o2 = "taobao";
        Glossary.StringLT order = new Glossary.StringLT();

        assertTrue(order.compare(o1, o2) > 0);

    }

    /**
     * Test compare method under subclass StringLT when o1< o2.
     */
    @Test
    public void testCompare2() {
        String o1 = "assert";
        String o2 = "taobao";
        Glossary.StringLT order = new Glossary.StringLT();

        assertTrue(order.compare(o1, o2) < 0);

    }

    /**
     * Test compare method under subclass StringLT when o1< o2. and o1 and o2
     * have same initial letter.
     */
    @Test
    public void testCompare3() {
        String o1 = "apple";
        String o2 = "assert";
        Glossary.StringLT order = new Glossary.StringLT();

        assertTrue(order.compare(o1, o2) < 0);

    }

    /**
     * Test compare method under subclass StringLT when o1= o2.
     */
    @Test
    public void testCompare4() {
        String o1 = "apple";
        String o2 = "apple";
        Glossary.StringLT order = new Glossary.StringLT();

        assertTrue(order.compare(o1, o2) == 0);

    }

    /**
     * Test method createGlossary.filePath is relative path, text file contain
     * multiple words and definition blocks.
     */
    @Test
    public void testCreateGlossary() {

        //create useful variables
        String filePath = "data/terms.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> expected = new Map1L<>();
        expected.add("meaning",
                "something that one wishes to convey, especially by language");
        expected.add("term", "a word whose definition is in a glossary");
        expected.add("word",
                "a string of characters in a language, which has at least one character");
        expected.add("definition", "a sequence of words that gives meaning to a term");
        expected.add("glossary",
                "a list of difficult or specialized terms, with their definitions, "
                        + "usually near the end of a book");
        expected.add("book", "a printed or written literary work");
        expected.add("language",
                "a set of strings of characters, each of which has meaning");

        assertEquals(expected.value("glossary"), glossary.value("glossary"));
        assertEquals(expected, glossary);

    }

    /**
     * Test method createGlossary text file only contain one words and
     * definition block.
     */
    @Test
    public void testCreateGlossary2() {
        //create useful variables
        String filePath = "test/TestFileForCreateGlossary2.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> expected = new Map1L<>();
        expected.add("meaning",
                "something that one wishes to convey, especially by language");

        assertEquals(expected.value("meaning"), glossary.value("meaning"));
        assertEquals(expected, glossary);

    }

    /**
     * Test method createGlossary text file contain a word with definition which
     * has multiple lines.
     */
    @Test
    public void testCreateGlossary3() {

        //create useful variables
        String filePath = "test/TestFileForCreateGlossary3.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> expected = new Map1L<>();
        expected.add("video",
                "a program, movie, or other visual media product featuring "
                        + "moving images, with or without audio, that is recorded and "
                        + "saved digitally or on videocassette: She used her phone to "
                        + "record a video of her baby's first steps.");

        assertEquals(expected.value("video"), glossary.value("video"));
        assertEquals(expected, glossary);

    }

    /**
     * Test method linkingWord. definition only has one word need to add link
     */
    @Test
    public void testLinkingWord() {
        //create useful variables
        String filePath = "data/terms.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> glossaryExpected = Glossary.createGlossary(filePath);
        Map<String, String> linkMap = Glossary.createLinkMap(glossary);

        String actual = Glossary.linkingWords(
                "something that one wishes to convey, especially by language", glossary,
                linkMap);
        String expected = "something that one wishes to convey, especially by "
                + "<a href=\"language.html\">language</a>";

        //make sure result matches expected result and make sure parameter
        //is not changed
        assertEquals(expected, actual);
        assertEquals(glossaryExpected, glossary);

    }

    /**
     * Test method linkingWord. definition contain no word need to add link
     */
    @Test
    public void testLinkingWord2() {

        //create useful variables
        String filePath = "data/terms.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> glossaryExpected = Glossary.createGlossary(filePath);

        Map<String, String> linkMap = Glossary.createLinkMap(glossary);
        String actual = Glossary.linkingWords("A random sentence, no need to add links",
                glossary, linkMap);
        String expected = "A random sentence, no need to add links";

        //make sure result matches expected result and make sure parameter
        //is not changed
        assertEquals(expected, actual);
        assertEquals(glossaryExpected, glossary);

    }

    /**
     * Test method linkingWord. definition contain multiple words need to add
     * link
     */
    @Test
    public void testLinkingWord3() {
        //create useful variables
        String filePath = "data/terms.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> glossaryExpected = Glossary.createGlossary(filePath);
        Map<String, String> linkMap = Glossary.createLinkMap(glossary);

        String actual = Glossary.linkingWords(
                "a word whose definition is " + "in a glossary", glossary, linkMap);
        String expected = "a <a href=\"word.html\">word</a> whose "
                + "<a href=\"definition.html\">definition</a> is in a "
                + "<a href=\"glossary.html\">glossary</a>";

        //make sure result matches expected result and make sure parameter
        //is not changed
        assertEquals(expected, actual);
        assertEquals(glossaryExpected, glossary);

    }

    /**
     * Test method OutPutTermPage with word meaning's detailed page.
     */
    @Test
    public void testOuputTermPage() {

        //create useful variables
        SimpleWriter out = new SimpleWriter1L(
                "test/ActualOutputOfTestOutputTermPage.html");
        String filePath = "data/terms.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> glossaryExpected = Glossary.createGlossary(filePath);
        Map<String, String> linkMap = Glossary.createLinkMap(glossary);
        Map<String, String> linkMapExpected = Glossary.createLinkMap(glossary);

        Glossary.outputTermPage(glossary, "meaning", out, linkMap);

        //read all lines from ActualOutputOfTestOutputTermPage.html into a String
        StringBuilder actual = new StringBuilder();
        SimpleReader in = new SimpleReader1L(
                "test/ActualOutputOfTestOutputTermPage.html");
        while (!in.atEOS()) {
            actual.append(in.nextLine());
        }

        //do the same thing for expected html file
        StringBuilder expected = new StringBuilder();
        SimpleReader in1 = new SimpleReader1L(
                "test/ExpectedOutputOfTestOutputTermPage.html");
        while (!in1.atEOS()) {
            expected.append(in1.nextLine());
        }
        in1.close();
        in.close();

        //compare the string of the files also make sure maps are unchanged.
        assertEquals(expected.toString(), actual.toString());
        assertEquals(glossaryExpected, glossary);
        assertEquals(linkMapExpected, linkMap);

    }

    /**
     * Test method OutPutIndex when the glossary contains multiple terms.
     */
    @Test
    public void testOuputIndexPage() {

        //create useful variables
        SimpleWriter out = new SimpleWriter1L(
                "test/ActualOutputOfTestOutputIndexPage.html");
        String filePath = "data/terms.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> glossaryExpected = Glossary.createGlossary(filePath);
        Map<String, String> linkMap = Glossary.createLinkMap(glossary);
        Map<String, String> linkMapExpected = Glossary.createLinkMap(glossary);

        Glossary.outputIndexPage(glossary, out, linkMap);

        //read all lines from ActualOutputOfTestOutputIndexPage.html into a String
        StringBuilder actual = new StringBuilder();
        SimpleReader in = new SimpleReader1L(
                "test/ActualOutputOfTestOutputIndexPage.html");
        while (!in.atEOS()) {
            actual.append(in.nextLine());
        }

        //do the same thing for expected html file
        StringBuilder expected = new StringBuilder();
        SimpleReader in1 = new SimpleReader1L(
                "test/ExpectedOutputOfTestOutputIndexPage.html");
        while (!in1.atEOS()) {
            expected.append(in1.nextLine());
        }
        in1.close();
        in.close();

        //compare the string of the files also make sure maps are unchanged.
        assertEquals(expected.toString(), actual.toString());
        assertEquals(glossaryExpected, glossary);
        assertEquals(linkMapExpected, linkMap);

    }

    /**
     * Test method OutPutIndex when the glossary contains only one terms.
     */
    @Test
    public void testOuputIndexPage2() {

        //create useful variables
        SimpleWriter out = new SimpleWriter1L(
                "test/ActualOutputOfTestOutputIndexPage2.html");
        String filePath = "test/GlossaryTextFileForTestOutputIndexPage2.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> glossaryExpected = Glossary.createGlossary(filePath);
        Map<String, String> linkMap = Glossary.createLinkMap(glossary);
        Map<String, String> linkMapExpected = Glossary.createLinkMap(glossary);

        Glossary.outputIndexPage(glossary, out, linkMap);

        //read all lines from ActualOutputOfTestOutputIndexPage.html into a String
        StringBuilder actual = new StringBuilder();
        SimpleReader in = new SimpleReader1L(
                "test/ActualOutputOfTestOutputIndexPage2.html");
        while (!in.atEOS()) {
            actual.append(in.nextLine());
        }

        //do the same thing for expected html file
        StringBuilder expected = new StringBuilder();
        SimpleReader in1 = new SimpleReader1L(
                "test/ExpectedOutputOfTestOutputIndexPage2.html");
        while (!in1.atEOS()) {
            expected.append(in1.nextLine());
        }
        in1.close();
        in.close();

        //compare the string of the files also make sure maps are unchanged.
        assertEquals(expected.toString(), actual.toString());
        assertEquals(glossaryExpected, glossary);
        assertEquals(linkMapExpected, linkMap);

    }

    /**
     * Test method OutPutIndex when the glossary contains multiple
     * terms(different glossary).
     */
    @Test
    public void testOuputIndexPage3() {

        //create useful variables
        SimpleWriter out = new SimpleWriter1L(
                "test/ActualOutputOfTestOutputIndexPage3.html");
        String filePath = "test/GlossaryForOutputIndexPage3.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> glossaryExpected = Glossary.createGlossary(filePath);
        Map<String, String> linkMap = Glossary.createLinkMap(glossary);
        Map<String, String> linkMapExpected = Glossary.createLinkMap(glossary);

        Glossary.outputIndexPage(glossary, out, linkMap);

        //read all lines from ActualOutputOfTestOutputIndexPage.html into a String
        StringBuilder actual = new StringBuilder();
        SimpleReader in = new SimpleReader1L(
                "test/ActualOutputOfTestOutputIndexPage3.html");
        while (!in.atEOS()) {
            actual.append(in.nextLine());
        }

        //do the same thing for expected html file
        StringBuilder expected = new StringBuilder();
        SimpleReader in1 = new SimpleReader1L(
                "test/ExpectedOutputOfTestOutputIndexPage3.html");
        while (!in1.atEOS()) {
            expected.append(in1.nextLine());
        }
        in1.close();
        in.close();

        //compare the string of the files also make sure maps are unchanged.
        assertEquals(expected.toString(), actual.toString());
        assertEquals(glossaryExpected, glossary);
        assertEquals(linkMapExpected, linkMap);

    }

    /**
     * Test method createLinkMap. Parameter map contain multiple items
     */
    @Test
    public void testCreateLinkMap() {

        //create useful variables
        String filePath = "data/terms.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> glossaryExpected = Glossary.createGlossary(filePath);
        Map<String, String> linkMap = Glossary.createLinkMap(glossary);
        Map<String, String> expected = new Map1L<>();
        expected.add("#indexPage", "<a href=\"index.html\">index</a>");
        expected.add("meaning", "<a href=\"meaning.html\">meaning</a>");
        expected.add("term", "<a href=\"term.html\">term</a>");
        expected.add("word", "<a href=\"word.html\">word</a>");
        expected.add("definition", "<a href=\"definition.html\">definition</a>");
        expected.add("glossary", "<a href=\"glossary.html\">glossary</a>");
        expected.add("book", "<a href=\"book.html\">book</a>");
        expected.add("language", "<a href=\"language.html\">language</a>");

        assertEquals(expected, linkMap);
        assertEquals(glossaryExpected, glossary);

    }

    /**
     * Test method createLinkMap. Parameter map contain only one pair.
     */
    @Test
    public void testCreateLinkMap1() {

        //create useful variables
        String filePath = "test/TestFileForCreateLinkMap1.txt";
        Map<String, String> glossary = Glossary.createGlossary(filePath);
        Map<String, String> glossaryExpected = Glossary.createGlossary(filePath);
        Map<String, String> linkMap = Glossary.createLinkMap(glossary);
        Map<String, String> expected = new Map1L<>();
        expected.add("#indexPage", "<a href=\"index.html\">index</a>");
        expected.add("term", "<a href=\"term.html\">term</a>");

        assertEquals(expected, linkMap);
        assertEquals(glossaryExpected, glossary);

    }

    /**
     * Test generateElement method with empty set as parameter.
     */
    @Test
    public void testGenerateElement() {
        String elements = "384728888344";
        String elementsExpected = "384728888344";
        Set<Character> ss = new Set1L<>();

        Glossary.generateElements(elements, ss);
        final int expectedSize = 5;
        assertEquals(expectedSize, ss.size());
        assertEquals(elementsExpected, elements);

    }

    /**
     * Test generateElement method with non-empty set parameter.
     */
    @Test
    public void testGenerateElement2() {
        String elements = "384728888344";
        String elementsExpected = "384728888344";
        Set<Character> ss = new Set1L<>();
        ss.add('5');
        ss.add('6');

        Glossary.generateElements(elements, ss);

        final int expectedSize = 5;
        assertEquals(expectedSize, ss.size());
        assertEquals(elementsExpected, elements);

    }

    /**
     * Test generateElement method with empty string as parameter.
     */
    @Test
    public void testGenerateElement3() {
        String elements = "";
        String elementsExpected = "";
        Set<Character> ss = new Set1L<>();

        Glossary.generateElements(elements, ss);
        final int expectedSize = 0;
        assertEquals(expectedSize, ss.size());
        assertEquals(elementsExpected, elements);

    }

    /**
     * Test testNextWordOrSeparator method with non-empty text,begin with word,
     * and also test different starting positions.
     */
    @Test
    public void testNextWordOrSeparator() {
        String text = "\"Because Mary and Samantha arrived at the bus station before "
                + "noon, I did not see them at the station\".";
        int position = 1;
        String separatorInString = "\" ,.";
        Set<Character> separator = new Set1L<>();
        Glossary.generateElements(separatorInString, separator);

        assertEquals("Because", Glossary.nextWordOrSeparator(text, position, separator));

        final int position2 = 9;
        assertEquals("Mary", Glossary.nextWordOrSeparator(text, position2, separator));

    }

    /**
     * Test testNextWordOrSeparator method with text starting with separator.
     */
    @Test
    public void testNextWordOrSeparator2() {

        String text = "\"Because Mary and Samantha arrived at the bus station before "
                + "noon, I did not see them at the station\".";
        int position = 0;
        String separatorInString = "\" ,.";
        Set<Character> separator = new Set1L<>();
        Glossary.generateElements(separatorInString, separator);

        Glossary.generateElements(separatorInString, separator);
        assertEquals("\"", Glossary.nextWordOrSeparator(text, position, separator));

    }

    /**
     * Test testNextWordOrSeparator method with text only contain word.
     */
    @Test
    public void testNextWordOrSeparator3() {

        String text = "ABCD";
        int position = 0;
        String separatorInString = "\" ,.";
        Set<Character> separator = new Set1L<>();
        Glossary.generateElements(separatorInString, separator);
        Glossary.generateElements(separatorInString, separator);

        assertEquals("ABCD", Glossary.nextWordOrSeparator(text, position, separator));

    }

    /**
     * Test testNextWordOrSeparator method with text only contain separator.
     */
    @Test
    public void testNextWordOrSeparator4() {
        String text = ",.,.,";
        int position = 0;
        String separatorInString = "\" ,.";
        Set<Character> separator = new Set1L<>();
        Glossary.generateElements(separatorInString, separator);
        Glossary.generateElements(separatorInString, separator);

        assertEquals(",.,.,", Glossary.nextWordOrSeparator(text, position, separator));

    }

}
