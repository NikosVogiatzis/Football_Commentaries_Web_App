package org.example;

import java.util.List;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.springframework.stereotype.Service;

@Service
public class UIClass {
    private final Repository repository;

    public UIClass() {
    	
        String repositoryUrl = "http://DESKTOP-4NI4QQ9:7200/repositories/football_commentaries";
        this.repository = new SPARQLRepository(repositoryUrl);
        this.repository.init();
    }

    public String executeQuery(String query) {
        StringBuilder resultBuilder = new StringBuilder();
        try (RepositoryConnection conn = repository.getConnection()) {
            TupleQuery tupleQuery = conn.prepareTupleQuery(query);
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                // Append variable names as the first row
                List<String> bindingNames = result.getBindingNames();
                for (String name : bindingNames) {
                    resultBuilder.append(name).append(" ");
                }
                resultBuilder.append("\n");

                // Append query results
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    for (String name : bindingNames) {
                        // Check if the value is null
                        Value value = bindingSet.getValue(name);
                        if (value != null) {
                            String stringValue = value.toString().replace(" ", "_");
                            resultBuilder.append(cleanPrefixValues(stringValue)).append(" ");
                        } else {
                            // Append an empty string or placeholder for null values
                            resultBuilder.append(" - ");
                        }
                    }
                    resultBuilder.append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultBuilder.toString();
    }

    
    

    
    private static String cleanPrefixValues(String value) {
        
        String prefix = "http://www.semanticweb.org/vogia/ontologies/2024/3/untitled-ontology-53//";
        
        if (value.startsWith(prefix)) {	
            return value.substring(prefix.length());  }
        else {
        	String regexPattern = "[\\'^\"$@]";
        	return value.replaceAll(regexPattern, "");
        }
    }
}