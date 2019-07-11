package com.springboot.vaadin.vaadin;

import com.springboot.vaadin.entity.Customer;
import com.springboot.vaadin.repository.CustomerRepository;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

@Route
public class MainView extends VerticalLayout {

    private final CustomerRepository repo;
    private final Grid<Customer> gridClick;
    private final Grid<Customer> gridText;

    private final CustomerEditor editor;
    private final Button addNewBtn;
    private final Grid<Customer> gridCustom;


    public MainView(CustomerRepository repo, CustomerEditor editor) {
        this.repo = repo;

        this.gridClick = new Grid<Customer>(Customer.class);
        listCustomers();

        this.gridText = new Grid<Customer>(Customer.class);
        // Initialize listing
        listCustomers(null);
        TextField filter = new TextField();
        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));
        add(new Button("查找与马有关的人", e -> {
                    Notification.show("正在查找与马有关的人！");
                    gridClick.setItems(repo.findByLastNameStartsWithIgnoreCase("马"));
                }),
                gridClick,
                filter,
                gridText);

        this.editor = editor;
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());
        this.gridCustom = new Grid<Customer>(Customer.class);
        gridCustom.setItems(repo.findAll());
        // build layout
        HorizontalLayout actions = new HorizontalLayout(addNewBtn);
        add(actions, gridCustom, editor);
        gridCustom.setHeight("300px");
        gridCustom.setColumns("id", "firstName", "lastName");
        gridCustom.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        // Connect selected Customer to editor or hide if none is selected
        gridCustom.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });
        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "")));
        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            gridCustom.setItems(repo.findAll());
        });
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
