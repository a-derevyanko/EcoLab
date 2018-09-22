package org.ecolab.client.vaadin.server.ui.view;

import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.EcoLabNavigator;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.api.View;
import org.ecolab.client.vaadin.server.ui.windows.AddTeacherGroupsWindow;
import org.ecolab.server.common.CurrentUser;
import org.ecolab.server.model.StudentGroupInfo;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.service.api.StudentInfoService;
import org.ecolab.server.service.api.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

@SpringView(name = TeacherGroupsManagingView.NAME)
public class TeacherGroupsManagingView extends GridLayout implements View {
  public static final String NAME = "teacher-account";

  private final AddTeacherGroupsWindow addTeacherGroupsWindow;

  private final UserInfoService userInfoService;

  private final StudentInfoService studentInfoService;

  private final EcoLabNavigator navigator;

  private final I18N i18N;

  // ---------------------------- Графические компоненты --------------------
  private final Label userInitialsLabel = new Label("Surname Firstname Lastname");
  private final Button downloadManualButton = new Button("Download manual", VaadinIcons.DOWNLOAD);
  private final Button methodMaterials = new Button("Methodic materials");
  private final Button lab1Journal = new Button("Lab 1 journal");
  private final Button lab2Journal = new Button("Lab 2 journal");
  private final Label todayDate = new Label();
  private final Grid<StudentGroupInfo> groups = new Grid<>();
  private final Button manageGroupButton = new Button("View group", event ->
          manageGroup(groups.getSelectedItems().iterator().next()));
  private final Button addGroupButton = new Button();
  private final Button removeGroupButton = new Button();
  private final HorizontalLayout buttons = new HorizontalLayout(addGroupButton, removeGroupButton);
  private final VerticalLayout labPresentationSelectContent = new VerticalLayout();
  private final PopupView popup = new PopupView(null, labPresentationSelectContent);

  @Autowired
  public TeacherGroupsManagingView(AddTeacherGroupsWindow addTeacherGroupsWindow,
                                   UserInfoService userInfoService,
                                   StudentInfoService studentInfoService, EcoLabNavigator navigator, I18N i18N) {
    super(5, 5);
    this.addTeacherGroupsWindow = addTeacherGroupsWindow;
    this.userInfoService = userInfoService;
    this.studentInfoService = studentInfoService;
    this.navigator = navigator;
    this.i18N = i18N;
  }

  @Override
  public void init() throws Exception {
    View.super.init();
    setMargin(true);
    setSpacing(true);

    setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

    addComponent(userInitialsLabel, 0, 0, 0, 0);
    addComponent(todayDate, 0, 1, 0, 1);
    addComponent(methodMaterials, 0, 2, 0, 2);
    addComponent(popup, 0, 3, 0, 3);
    addComponent(groups, 1, 0, 4, 3);
    addComponent(manageGroupButton, 1, 4, 1, 4);
    addComponent(buttons, 3, 4, 4, 4);
    setComponentAlignment(manageGroupButton, Alignment.MIDDLE_LEFT);
    setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);

    groups.setSizeFull();

    groups.setSelectionMode(Grid.SelectionMode.SINGLE);
    groups.addItemClickListener(event -> {
      if (event.getMouseEventDetails().isDoubleClick()) {
        manageGroup(event.getItem());
      }
    });

    groups.addSelectionListener(event -> {
      if (event.getAllSelectedItems().isEmpty()) {
        manageGroupButton.setEnabled(false);
        removeGroupButton.setEnabled(false);
      } else {
        manageGroupButton.setEnabled(true);
        removeGroupButton.setEnabled(true);
      }
    });

    manageGroupButton.setEnabled(false);
    addGroupButton.setCaption(i18N.get("student-data.add-group"));
    addGroupButton.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
    addGroupButton.addClickListener(event -> {
      Set<StudentGroupInfo> selected = groups.getSelectedItems();
      addTeacherGroupsWindow.show(new AddTeacherGroupsWindow.AddTeacherGroupsWindowSettings(CurrentUser.getId(), () -> {
        refreshTeacherGroups(selected);
      }));
    });

    removeGroupButton.setEnabled(false);
    removeGroupButton.setCaption(i18N.get("student-data.remove-group"));
    removeGroupButton.setStyleName(EcoLabTheme.BUTTON_DANGER);
    removeGroupButton.addClickListener(event -> {
      Set<StudentGroupInfo> selected = groups.getSelectedItems();
      if (!selected.isEmpty()) {
        selected.forEach(s -> studentInfoService.removeGroupFromTeacher(CurrentUser.getId(), s.getName()));
        refreshTeacherGroups(selected);
      }
    });

    manageGroupButton.setCaption(i18N.get("teacher-group-manage.view-group"));
    manageGroupButton.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
    Grid.Column<StudentGroupInfo, Integer> number = groups.addColumn(StudentGroupInfo::getIndex).setCaption(i18N.get("teacher-group-manage.groups.number"));
    groups.addColumn(StudentGroupInfo::getName).setCaption(i18N.get("teacher-group-manage.groups.name"));
    groups.addColumn(StudentGroupInfo::getStudentCount).setCaption(i18N.get("teacher-group-manage.groups.student-count"));
    groups.addColumn(studentGroupInfo -> studentGroupInfo.getLabCount() + "/3").setCaption(i18N.get("teacher-group-manage.groups.lab-count"));
    groups.addColumn(studentGroupInfo -> studentGroupInfo.getDefenceCount() + "/3").setCaption(i18N.get("teacher-group-manage.groups.defence-count"));

    groups.setSortOrder(Collections.singletonList(new GridSortOrder<>(number, SortDirection.ASCENDING)));

    userInitialsLabel.setContentMode(ContentMode.HTML);
    todayDate.setContentMode(ContentMode.HTML);

        /*new BrowserWindowOpener(new DownloadStreamResource(
                () -> settings.labService.printInitialData(settings.variant, UI.getCurrent().getLocale()),
                "initialData.pdf")).extend(downloadManualButton);*/


    createPresentationButton(lab1Journal, i18N.get("teacher-group-manage.download-lab-journal-1"), 1);
    createPresentationButton(lab2Journal, i18N.get("teacher-group-manage.download-lab-journal-2"), 2);

    methodMaterials.setCaption(i18N.get("teacher-group-manage.download-manual"));
    methodMaterials.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
    methodMaterials.addClickListener(event ->  popup.setPopupVisible(true));
    downloadManualButton.setCaption(i18N.get("teacher-group-manage.download-manual-teacher"));
    downloadManualButton.setHeight(45.0F, Unit.PIXELS);
    downloadManualButton.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
    downloadManualButton.setSizeFull();

    FileDownloader fileDownloader = new FileDownloader(new StreamResource(
            () -> getClass().getClassLoader().getResourceAsStream("org/ecolab/server/teacher-manual.pdf"), "manual.pdf"));

    fileDownloader.extend(downloadManualButton);

    labPresentationSelectContent.addComponent(downloadManualButton);
    labPresentationSelectContent.addComponent(lab1Journal);
    labPresentationSelectContent.addComponent(lab2Journal);
    popup.setHideOnMouseOut(true);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    refreshTeacherGroups(Collections.emptySet());
    UserInfo userProfile = userInfoService.getUserInfo(CurrentUser.getId());

    todayDate.setValue(i18N.get("teacher-group-manage.today-date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            "" /*LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm"))*/));

    userInitialsLabel.setValue(i18N.get("teacher-group-manage.initials", userProfile.getLastName(),
            userProfile.getFirstName(), userProfile.getMiddleName()));
  }

  private void refreshTeacherGroups(@NotNull Set<StudentGroupInfo> selected) {
    groups.setItems(studentInfoService.getTeacherGroupsInfo(CurrentUser.getId()));
    if (selected.isEmpty()) {
      removeGroupButton.setEnabled(false);
    } else {
      selected.forEach(groups::select);
      removeGroupButton.setEnabled(true);
    }
  }

  private void manageGroup(StudentGroupInfo group) {
    navigator.redirectToView(GroupManagingView.NAME + '/' + GroupManagingView.GROUP_NAME + '=' + group.getName());
  }

  private void createPresentationButton(Button button, String title, int labNumber) {
    FileDownloader fileDownloader = new FileDownloader(new StreamResource(
            () -> getClass().getClassLoader().getResourceAsStream("org/ecolab/server/service/impl/content/lab" + labNumber + "/experiment/report/experimentJournal.pdf"), "manual.pdf"));

    fileDownloader.extend(button);
    button.setCaption(title);
    button.setHeight(45.0F, Unit.PIXELS);
    button.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
    button.setSizeFull();
  }
}
