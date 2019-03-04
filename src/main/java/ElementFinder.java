import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Stack;

public class ElementFinder {
  private static final String CHARSET_NAME = "utf8";
  private static final String targetElementId = "make-everything-ok-button";
  private static File originalFile;
  private static File otherFile;

  public static void main(String[] args) {
    String result = "";

    if (args.length != 0 && args[0] != null && args[1] != null) {
      setOriginalFile(args[0]);
      setOtherFile(args[1]);
    } else throw new IllegalArgumentException("Please provide legal input arguments");

    Optional<Element> buttonOpt = findElementById(originalFile, targetElementId);
    String searchCriteria = buttonOpt.get().attributes().get("title");
    Optional<Collection<Element>> col = findElementByTitle(otherFile ,searchCriteria);

    Collection<Element> elements = col.get();

    for (Element e : elements) {
      printParents(e);
    }
  }

  private static void printParents(Element e) {
    Stack<String> stack = new Stack<>();
    stack.push(e.tagName());
    Element startingEl = e;

    while (startingEl.hasParent()) {

      if (!startingEl.parent().tagName().equals("#root")) {
        stack.push(startingEl.parent().tagName() + "->");
      }
      startingEl = startingEl.parent();
    }

    while (!stack.empty()) {
      System.out.print(stack.pop());
    }
  }

  private static void setOriginalFile(String path) {
    if(path != null) {
      originalFile = new File(path);
    }
  }

  private static void setOtherFile(String path) {
    if (path != null) {
      otherFile = new File(path);
    }
  }

  private static Optional<Element> findElementById(File htmlFile, String targetElementId) {
    try {
      Document doc = Jsoup.parse(
              htmlFile,
              CHARSET_NAME,
              htmlFile.getAbsolutePath());

      return Optional.of(doc.getElementById(targetElementId));

    } catch (IOException e) {
      System.out.println(("Error reading [{}] file " + htmlFile.getAbsolutePath() + " " + e));
      return Optional.empty();
    }
  }

  private static Optional<Collection<Element>> findElementByTitle(File htmlFile, String targetElementTitle) {
    try {
      Document doc = Jsoup.parse(
              htmlFile,
              CHARSET_NAME,
              htmlFile.getAbsolutePath());

      return Optional.of(doc.getElementsByAttributeValueContaining("title", targetElementTitle));

    } catch (IOException e) {
      System.out.println(("Error reading [{}] file " + htmlFile.getAbsolutePath() + " " + e));
      return Optional.empty();
    }
  }
}

