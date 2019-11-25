package pl.milk.aggregator.persistance.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class TestContainer {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "testContainer_Sequence")
    @SequenceGenerator(name = "testContainer_Sequence", sequenceName = "TESTCONTAINER_SEQ")
    private Long id;

    @Column(name = "CONTAINER_NAME")
    private String name;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "testContainer")
    private List<Test> tests;

    public TestContainer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }
}
