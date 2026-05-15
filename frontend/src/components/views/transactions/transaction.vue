<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
const transaction = ref(null)
onMounted(async () => {
try {
const result = await axios.get("/api/transactions/{id}")
transaction.value = result.data
} catch (error) {
console.log(error)
}
})

</script>

<template>
    <div class="text-center">
        <h1 class="display-4">transaction</h1>

        <div >
               
            <p> {{ transaction.FromAccount }} </p>
            <p> {{ transaction.ToAccount }} </p>
            <p> {{ transaction.Amount }} </p>
            <p> {{ transaction.Time }} </p>
            <p> {{ transaction.Type }} </p>
            <p> {{ transaction.IniciatedBy }} </p>
            <p> {{ transaction.Message }} </p>      
        </div>
    
    </div>
 
</template>
