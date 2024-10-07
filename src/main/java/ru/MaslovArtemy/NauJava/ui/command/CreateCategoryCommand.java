package ru.MaslovArtemy.NauJava.ui.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.output.Printer;
import ru.MaslovArtemy.NauJava.service.CategoryService;

import java.util.Optional;

@Component
public class CreateCategoryCommand implements Command {
    private final CategoryService categoryService;
    private final Printer printer;

    @Autowired
    public CreateCategoryCommand(CategoryService categoryService, Printer printer) {
        this.categoryService = categoryService;
        this.printer = printer;
    }

    @Override
    public String getName() {
        return "createCategory";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            printer.print("Используй: createCategory <username> <password>\n");
            return;
        }

        String name = args[0].toLowerCase();
        String desc = args[1];

        Optional<Category> category = categoryService.getCategoryByName(name);

        category.ifPresentOrElse(c -> printer.print("Такая категория уже есть!\n"),
                () -> {
                    Category category1 = categoryService.createCategory(name, desc);
                    printer.print("Категория " + category1.getName() + " успешно создана!\n");
                });
    }
}
