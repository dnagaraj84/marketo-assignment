# marketo-assignment
 Removing the duplicate records from the JSON based on the given rules.
 
# Problem Statement
 Remove the duplicate records from the given JSON based on the following rules.
1.	The data from the newest date should be preferred
2.	duplicate IDs count as dups. Duplicate emails count as dups. Both must be unique in our dataset. Duplicate values elsewhere do not count as dups.
3.	If the dates are identical the data from the record provided last in the list should be preferred.

# Approach taken
For the given requirement, duplicates can be found either record having same ID or Email ID. Initial thought process was to create data dictionary with two HashMap. One HashMap containing ID as key and Object as value and another HashMap with Email as key and object as value. Start loading both these HashMap's when duplicate record is encountered ID/Email HashMap then business logic is used to find the right record and update the HashMap with it. At the end, we will have two HashMap without duplicates based on their keys. Now the problem arises, what happens if we have a duplicates which existed in both these HashMap but as single record in their corresponding HashMap based on their key ? In this case, Before while creating the final output i wil check for both the HashMap and if record exists in both the map then i will use the same business logic to find which is the right record. Based on this thought process dictionary was created.

The above approach gave me time complexity of O(2n). O(n) for creating the data dictionary and another O(n) to create the results.  In my second iteration of code, i want to reduce this time complexity from O(2n) to O(n). So instead of seperating the process of data creation and result generation i have decided to merge them both. This reduced my time complexity to O(n)

Problem solving time complexity is O(n*1) = O(n)

## Getting Started

### Installing 
```
git clone https://github.com/dnagaraj84/marketo-assignment.git
```

# Running the build and tests

Demo-able unit test to verify functionality & test the intregrity of the solution provided.

#### Unit Test
```
com.marketo.service.DataProcesserTest
com.marketo.service.JsonHelperTest
```

#### Build Artifact
```
mvn clean install
```

#### Deployment
```
java -jar target\marketo-assignment-0.0.1-SNAPSHOT.jar OR run the Application.java
```

## Libraries Used

Maven is configured to fetch these libraries.
```
log4j-1.2.17.jar for logging
json-simple-1.1.1.jar for parsing the JSON
junit-4.12.jar for JUnit test cases.
hamcrest-core-1.3.jar for JUnit test cases
```

## Built With
* Java
* Junit
* Maven
