<script setup>
const props = defineProps({
  userId: {
    type: Number,
    required: true
  }
})
</script>

<template>
  <RouterLink
    :to="{
      path: `/users/${userId}/bank-accounts`
    }"
  >
    Approve Customer
  </RouterLink>
</template>