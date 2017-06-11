package sanchez.sergio.domain;

import sanchez.sergio.persistence.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

import java.io.Serializable;

public class Users extends Resources<User> {

    private PageModel page;

    public Users(Page<User> accountPage) {
        super(accountPage.getContent());
        page = new PageModel(accountPage);
    }

    public Users(Iterable<User> content, Link... links) {
        super(content, links);
    }

    public Users(Iterable<User> content, Iterable<Link> links) {
        super(content, links);
    }

    public PageModel getPage() {
        return page;
    }

    class PageModel implements Serializable {

        private int number;
        private int size;
        private int totalPages;
        private long totalElements;

        public PageModel() {
        }

        public PageModel(Page page) {
            number = page.getNumber();
            size = page.getSize();
            totalPages = page.getTotalPages();
            totalElements = page.getTotalElements();
        }

        public int getNumber() {
            return number;
        }

        public int getSize() {
            return size;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public long getTotalElements() {
            return totalElements;
        }
    }
}
