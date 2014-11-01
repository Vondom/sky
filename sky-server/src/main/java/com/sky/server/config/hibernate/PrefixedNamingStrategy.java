package com.sky.server.config.hibernate;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;

/**
 * Created by jcooky on 14. 10. 28..
 */
public class PrefixedNamingStrategy extends ImprovedNamingStrategy {
  public static final NamingStrategy INSTANCE = new PrefixedNamingStrategy();
  private static final String PREFIX = "SKY_", PREFIX_LOWER = PREFIX.toLowerCase(), PREFIX_UPPER = PREFIX.toUpperCase();

  @Override
  public String tableName(String tableName) {
    return PREFIX_UPPER + super.tableName(tableName);
  }

  @Override
  public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable, String propertyName) {
    return PREFIX_UPPER + super.collectionTableName(ownerEntity, ownerEntityTable, associatedEntity, associatedEntityTable, propertyName);
  }

  @Override
  public String joinKeyColumnName(String joinedColumn, String joinedTable) {
    return PREFIX_LOWER + super.joinKeyColumnName(joinedColumn, joinedTable);
  }

  @Override
  public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
    return PREFIX_LOWER + super.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName, referencedColumnName);
  }

  @Override
  public String logicalColumnName(String columnName, String propertyName) {
    return PREFIX_LOWER + super.logicalColumnName(columnName, propertyName);
  }

  @Override
  public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {
    return PREFIX_LOWER + super.logicalCollectionTableName(tableName, ownerEntityTable, associatedEntityTable, propertyName);
  }

  @Override
  public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
    return PREFIX_LOWER + super.logicalCollectionColumnName(columnName, propertyName, referencedColumn);
  }

  @Override
  public String classToTableName(String className) {
    return PREFIX_UPPER + super.classToTableName(className);
  }

  @Override
  public String columnName(String columnName) {
    return PREFIX_LOWER + super.columnName(columnName);
  }

  @Override
  public String propertyToColumnName(String propertyName) {
    return PREFIX_LOWER + super.propertyToColumnName(propertyName);
  }
}
