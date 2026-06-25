<script setup>
import { ref } from 'vue'

const props = defineProps({
  label: String,
  filterName: String,
  instant: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['filter-change'])

const open = ref(false)
const value = ref('')
function handleClick() {
  if (props.instant) {
    emit('filter-change', {
      filterName: props.filterName,
      filterValue: null
    })
    return
  }
  else
  {
    open.value = !open.value
  }
}
</script>

<template>
  <div class="d-inline-block position-relative me-2 mb-2">

    <!-- knop -->
    <button class="btn btn-primary" @click="handleClick">
      {{ label }}
    </button>

    <!-- mini popup -->
    <div v-if="open" class="shadow p-2 border bg-white rounded popup">

      <input
      type="date"
        v-model="value"
        class="form-control form-control-sm mb-2"
        placeholder="Value..."
      />

      <button
        class="btn btn-sm btn-success w-100"
        @click="
          emit('filter-change', {
            filterName,
            filterValue: value
          });
          open = false
        "
      >
        Apply
      </button>

    </div>

  </div>
</template>

<style scoped>
.popup {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 999;
  width: 200px;
}
</style>