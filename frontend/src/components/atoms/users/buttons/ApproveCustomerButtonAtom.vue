<script setup>
const props = defineProps({
  user: {
    type: Object,
    required: true
  }
})

const emits = defineEmits(['approveCustomer']);
</script>

<template>
  <td>
  <RouterLink @click="$emit('approveCustomer', user.userId)"
    :to="{
      path: `/users/${user.userId}/bank-accounts`
    }"
  >
    Approve Customer
  </RouterLink>
  </td>
</template>