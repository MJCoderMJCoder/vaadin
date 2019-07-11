package com.springboot.vaadin.vaadin;

import com.springboot.vaadin.entity.Customer;
import com.springboot.vaadin.repository.CustomerRepository;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

@Route
public class MainView extends VerticalLayout {
//    public MainView() {
//        add(new Button("Click me", e -> Notification.show("Hello Spring+Vaadin user!")));
//    }

    private CustomerRepository repo;
    private Grid<Customer> gridClick;
    private Grid<Customer> gridText;

    public MainView(CustomerRepository repo) {
        this.repo = repo;

        this.gridClick = new Grid<>(Customer.class);
        listCustomers();

        this.gridText = new Grid<>(Customer.class);
        TextField filter = new TextField();
        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        add(new Button("Click me", e -> gridClick.setItems(repo.findByLastNameStartsWithIgnoreCase("马"))), gridClick, filter, gridText);
    }

    private void listCustomers() {
        gridClick.setItems(repo.findAll());
    }

    private void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            gridText.setItems(repo.findAll());
        } else {
            gridText.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
        }
    }
}
