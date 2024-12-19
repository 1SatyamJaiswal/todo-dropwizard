package org.satyam.todolist;
import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.satyam.todolist.models.Todo;
import org.satyam.todolist.resources.TodoResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class App extends Application<TodoListConfiguration> {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    public void initialize(Bootstrap<TodoListConfiguration> bootstrap) {

    }

    public void run(TodoListConfiguration configuration, Environment environment) throws Exception {
        log.info("Initializing App");
        //Create a DBI Factory and JDBI Instance
        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
        //Create DataSource using the DataSourceFactory
        DataSource dataSource = dataSourceFactory.build(environment.metrics(), "mysql");
        final Jdbi jdbi = Jdbi.create(dataSource);

        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.registerRowMapper(BeanMapper.factory(Todo.class));
        // Add the resource to the environment
        environment.jersey().register(new TodoResource(jdbi));
    }
}
