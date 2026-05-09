<script setup>
import BSNNumberDataFieldAtom from '../../../atoms/users/textelements/datafields/BSNNumberDataFieldAtom.vue';
import EmailDataFieldAtom from '../../../atoms/users/textelements/datafields/EmailDataFieldAtom.vue';
import FirstNameDataFieldAtom from '../../../atoms/users/textelements/datafields/FirstNameDataFieldAtom.vue';
import LastNameDataFieldAtom from '../../../atoms/users/textelements/datafields/LastNameDataFieldAtom.vue';
import PhoneNumberDataFieldAtom from '../../../atoms/users/textelements/datafields/PhoneNumberDataFieldAtom.vue';
import ApproveCustomerButtonAtom from '../../../atoms/users/buttons/ApproveCustomerButtonAtom.vue';

const props = defineProps({
    user: {
        type: Object,
        required: true,
        validator: (value) => {
            return typeof value.userId === 'number' &&
            typeof value.firstName === 'string' &&
            typeof value.lastName === 'string' &&
            typeof value.email === 'string' &&
            typeof value.bsn === 'string' &&
            typeof value.phoneNumber === 'string' &&
            typeof value.role === 'string' &&
            typeof value.isActive === 'boolean' &&
            typeof value.isApproved === 'boolean'
        }
    }
})

</script>

<template>
  <tr>
    <FirstNameDataFieldAtom :user="user" />
    <LastNameDataFieldAtom :user="user" />
    <EmailDataFieldAtom :user="user" />
    <BSNNumberDataFieldAtom :user="user" />
    <PhoneNumberDataFieldAtom :user="user" />
    <ApproveCustomerButtonAtom />
    </tr>  
</template>