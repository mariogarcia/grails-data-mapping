package grails.gorm.tests

import grails.persistence.Entity

class ConstraintsSpec extends GormDatastoreSpec {

    void "Test constraints with static default values"() {
         given: "A Test class with static constraint values"
            def ce = new ConstrainedEntity(num:1000, str:"ABC")

         when: "saved is called"
            ce.save()

         then:
            ce.hasErrors() == false
    }

    @Override
    List getDomainClasses() {
        [ConstrainedEntity]
    }
}

@Entity
class ConstrainedEntity implements Serializable {

    static final int MAX_VALUE = 1000
    static final List<String> ALLOWABLE_VALUES = ['ABC','DEF','GHI']

    Long id
    Integer num
    String str

    static constraints = {

        num(maxSize: MAX_VALUE) /*Must be MyDomainClass.MAX_VALUE in order work with redis*/
        str validator: { val, obj ->
            if (val != null && !ALLOWABLE_VALUES.contains(val)) {/*Must be MyDomainClass.ALLOWABLE_VALUES in order work with redis */
                return ['not.valid']
            }
        }
    }
}
