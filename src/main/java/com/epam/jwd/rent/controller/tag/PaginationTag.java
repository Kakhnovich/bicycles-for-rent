package com.epam.jwd.rent.controller.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Custom tag for pagination for tables
 * @see TagSupport
 * @author Elmax19
 * @version 1.0
 */
public class PaginationTag extends TagSupport {
    /**
     * current page number - default value is 1
     */
    private int pageNumber = 1;
    /**
     * count of table pages
     */
    private int countOfPages;
    /**
     * servlet command
     * @see com.epam.jwd.rent.command.CommandManager
     */
    private String command;
    /**
     * active sort column
     */
    private String column;

    /**
     * set new value for {@link PaginationTag#pageNumber}
     * @param pageNumber actual page
     */
    public void setPageNumber(String pageNumber) {
        this.pageNumber = Integer.parseInt(pageNumber);
    }

    /**
     * set new value for {@link PaginationTag#countOfPages}
     * @param countOfPages count of possible pages
     */
    public void setCountOfPages(String countOfPages) {
        this.countOfPages = Integer.parseInt(countOfPages);
    }

    /**
     * set new value for {@link PaginationTag#command}
     * @param command actual servlet command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * set new value for {@link PaginationTag#column}
     * @param column actual sort column
     */
    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().write("<div>");
            if (pageNumber > 1) {
                if (pageNumber > 2) {
                    pageContext.getOut().write("<a class='number' href=\"/controller?command=" + command + "&page=1&column=" + column + "\"> 1 </a>");
                    if (pageNumber > 3) {
                        pageContext.getOut().write(" ... ");
                    }
                }
                pageContext.getOut().write("<a class='number' href=\"/controller?command=" + command + "&page=" + (pageNumber - 1) + "&column=" + column + "\"> " + (pageNumber - 1) + " </a>");
            }
            pageContext.getOut().write(" <b>" + pageNumber + "</b> ");
            if (countOfPages > pageNumber) {
                pageContext.getOut().write("<a class='number' href=\"/controller?command=" + command + "&page=" + (pageNumber + 1) + "&column=" + column + "\"> " + (pageNumber + 1) + " </a>\n");
                if (countOfPages > pageNumber + 1) {
                    if (countOfPages > pageNumber + 2) {
                        pageContext.getOut().write(" ... ");
                    }
                    pageContext.getOut().write("<a class='number' href=\"/controller?command=" + command + "&page=" + countOfPages + "&column=" + column + "\"> " + countOfPages + " </a>");
                }
            }
            pageContext.getOut().write("</div>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}