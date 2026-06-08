<script setup>
import ApproveCustomerButtonAtom from "@/atoms/users/buttons/ApproveCustomerButtonAtom.vue";
import BaseTableDataFieldAtom from "../../../atoms/bankaccounts/textelements/datafields/BaseTableDataFieldAtom.vue";

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

const emits = defineEmits(['approveCustomer']);
</script>

<template>
  <tr class="table table-light">
    <BaseTableDataFieldAtom :text="user.firstName" />
    <BaseTableDataFieldAtom :text="user.lastName" />
    <BaseTableDataFieldAtom :text="user.email" />
    <BaseTableDataFieldAtom :text="user.bsn" />
    <BaseTableDataFieldAtom :text="user.phoneNumber" />
    <ApproveCustomerButtonAtom v-if="user.role !== 'EMPLOYEE'" @approveCustomer="$emit('approveCustomer')" :user="user"/>
    </tr>  
</template>