package tech.ival.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

import javax.persistence.*;

@Entity
@Table(name = "characters")
public class Character extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String nickname;
    public String role;
    public Integer deff;
    public Integer physicalDmg;
    public Integer magicalDmg;

}
