package org.graphdb.jena.tutorial;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SimpleInsertQueryExample {
  private static Logger logger = LoggerFactory.getLogger(SimpleInsertQueryExample.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
  
  // GraphDB 
  private static final String PERSONDATA_REPO_QUERY = 
      "http://localhost:7200/repositories/PersonData";
  private static final String PERSONDATA_REPO_UPDATE = 
      "http://localhost:7200/repositories/PersonData/statements";


  private static String strInsert;
  private static String strQuery;
  
  static {
    strInsert = 
        "INSERT DATA {"
         + "<http://dbpedia.org/resource/Grace_Hopper> "
         + "<http://dbpedia.org/ontology/birthDate>"
         + " \"1906-12-09\"^^<http://www.w3.org/2001/XMLSchema#date> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> "
         + "<http://dbpedia.org/ontology/birthPlace> "
         + "<http://dbpedia.org/resource/New_York_City> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> "
         + "<http://dbpedia.org/ontology/deathDate>"
         + " \"1992-01-01\"^^<http://www.w3.org/2001/XMLSchema#date> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> "
         + "<http://dbpedia.org/ontology/deathPlace> "
         + "<http://dbpedia.org/resource/Arlington_County,_Virginia> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> "
         + "<http://purl.org/dc/terms/description>"
         + " \"American computer scientist and United States Navy officer.\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> "
         + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
         + "<http://dbpedia.org/ontology/Person> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> "
         + "<http://xmlns.com/foaf/0.1/gender> \"female\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> "
         + "<http://xmlns.com/foaf/0.1/givenName> \"Grace\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> "
         + "<http://xmlns.com/foaf/0.1/name> \"Grace Hopper\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper>"
         + " <http://xmlns.com/foaf/0.1/surname> \"Hopper\" ."        
         + "}";
    
    strQuery = 
        "SELECT ?name WHERE {?s <http://xmlns.com/foaf/0.1/name> ?name .}";
  }   
  
  private static void insert() {
    UpdateRequest updateRequest = UpdateFactory.create(strInsert);
    UpdateProcessor updateProcessor = UpdateExecutionFactory
        .createRemote(updateRequest, 
        PERSONDATA_REPO_UPDATE);
    updateProcessor.execute();
  }
  
  private static void query() {
    QueryExecution queryExecution = QueryExecutionFactory
        .sparqlService(PERSONDATA_REPO_QUERY, strQuery);
    for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
      QuerySolution qs = results.next();
      String strName = qs.get("?name").toString();
      logger.trace("name = " + strName);
    }    
    queryExecution.close();
  }

  
  public static void main(String[] args) {
    try {
      insert(); 
      query();      
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }  
}
