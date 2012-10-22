package aaf.base.identity

class RoleService {

  boolean transactional = true

  /**
   * Creates a new role.
   *
   * @param name Name to assign to group
   * @param description Description to assign to group
   * @param protect Boolean indicating if this is a protected group (True disables modification in UI)
   *
   * @throws RuntimeException When internal state requires transaction rollback
   */
  public Role createRole(String name, String description, boolean protect) {
    def role = new Role()
    role.name = name
    role.description = description
    role.protect = protect

    if(!role.validate()) {
      log.debug("Supplied values for new role are invalid")
      role.errors.each {
        log.debug it
      }
      return role
    }

    def savedRole = role.save()
    if (savedRole) {
      log.info("Created role [$role.id]$role.name")
      return savedRole
    }

    log.error("Error creating new group")
    role.errors.each {
      log.error it
    }

    throw new RuntimeException("Error creating new role, object persistance failed")
  }

  /**
   * Deletes an exisiting Role.
   *
   * @param role The role instance to be deleted
   *
   * @throws RuntimeException When internal state requires transaction rollback
   */
  public void deleteRole(Role role) {

    // Remove all subjects from this role
    def subjects = []
    subjects.addAll(role.subjects)
    subjects.each {
      it.removeFromRoles(role)

      if (!it.save()) {
        log.error("Error updating $it to remove $role")
        it.errors.each {err ->
          log.error err
        }

        throw new RuntimeException("Error updating $it to remove $role")
      }
    }

    role.delete()
    log.info("Deleted role [$role.id]$role.name")
  }

  /**
   * Updates an existing role.
   *
   * @param role The role to be updated.
   *
   * @throws RuntimeException When internal state requires transaction rollback
   */
  public Role updateRole(Role role) {

    def updatedRole = role.save()
    if (updatedRole) {
      log.info("Updated role [$role.id]$role.name")
      return updatedRole
    }

    log.error("Error updating role [$role.id]$role.name")
    role.errors.each {err ->
      log.error err
    }
    throw new RuntimeException("Error updating $role")
  }

  /**
   * Assigns a role to a subject.
   *
   * @param subject The subject whole the referenced role should be assigned to
   * @param role The role to be assigned
   *
   * @throws RuntimeException When internal state requires transaction rollback
   */
  public void addMember(Subject subject, Role role) {
    role.addToSubjects(subject)
    subject.addToRoles(role)

    if (!role.save()) {
      log.error("Error updating $role to add $subject")

      role.errors.each {
        log.error(it)
      }

      throw new RuntimeException("Error updating $role to add $subject")
    }
 
    if (!subject.save()) {
      log.error("Error updating $subject when adding to $role")

      subject.errors.each {
        log.error(it)
      }

      throw new RuntimeException("Error updating $subject when adding to $role")
    }

    log.info("Successfully added $role to $subject")
  }

  /**
   * Removes a role from a subject.
   *
   * @param subject The subject whole the referenced role should be removed from
   * @param role The role to be assigned
   * 
   * @throws RuntimeException When internal state requires transaction rollback
   */
  public void deleteMember(Subject subject, Role role) {
    role.removeFromSubjects(subject)
    subject.removeFromRoles(role)

    if (!role.save()) {
      log.error("Error updating $role to delete $subject")

      role.errors.each {
        log.error(it)
      }

      throw new RuntimeException("Error updating $role to delete $subject")
    }

    if (!subject.save()) {
      log.error("Error updating $subject to delete from $role")
      subject.errors.each {
        log.error(it)
      }

      throw new RuntimeException("Error updating $subject to delete from $role")
    }

    log.info("Successfully removed $subject from $role")
  }
}
