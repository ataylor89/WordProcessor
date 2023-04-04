package wordprocessor;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author andrewtaylor
 */
public class TabFilter extends DocumentFilter {
    private int tabSize;
    private String spaces;

    public TabFilter(int tabSize) {
        this.setTabSize(tabSize);
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        string = string.replace("\t", spaces);
        super.insertString(fb, offset, string, attr);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        text = text.replace("\t", spaces);
        super.replace(fb, offset, length, text, attrs);
    }
    
    public final void setTabSize(int tabSize) {
        this.tabSize = tabSize;
        spaces = "";
        for (int i = 0; i < tabSize; i++) {
            spaces += " ";
        }
    }

    public final int getTabSize() {
        return tabSize;
    }
    
    public Integer[] getTabSizes() {
        return new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    }
}
