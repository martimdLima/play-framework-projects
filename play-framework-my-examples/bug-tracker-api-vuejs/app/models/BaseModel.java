package models;

import io.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * BaseModel entity managed by Ebean
 */
@MappedSuperclass
public class BaseModel extends Model {
    @Id
    public Long id;
}
