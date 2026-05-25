<script setup>
const props = defineProps({
  userId: {
    type: Number,
    required: true
  }
})
</script>

<template>
  <td>
  <RouterLink
    :to="{
      path: `/users/${userId}/bank-accounts`
    }"
  >
    Approve Customer
  </RouterLink>
  </td>
</template>