package bookshopsystemapp.service;

import bookshopsystemapp.domain.entities.Category;
import bookshopsystemapp.repository.CategoryRepository;
import bookshopsystemapp.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final static String CATEGORIES_FILE_PATH = "C:\\Users\\Stefan\\IdeaProjects\\04.JavaHibernate\\Exercise_Spring_Advanced_Querying\\src\\main\\resources\\files\\categories.txt";

    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, FileUtil fileUtil) {
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedCategories() throws IOException {
        if (this.categoryRepository.count() != 0) {
            return;
        }

        String[] categoriesFileContent = this.fileUtil.getFileContent(CATEGORIES_FILE_PATH);
        for (String line : categoriesFileContent) {
            Category category = new Category();
            category.setName(line);

            this.categoryRepository.saveAndFlush(category);
        }
    }
}
