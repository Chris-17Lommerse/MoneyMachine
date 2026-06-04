import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
    base: '/MoneyMachine/',
      plugins: [
        vue()
      ],
      resolve: {
        alias: [
          {
            find: /^@\/components\//,
            replacement: fileURLToPath(new URL('./src/components/', import.meta.url)),
          },
          {
            find: /^@\/atoms\//,
            replacement: fileURLToPath(new URL('./src/components/atoms/', import.meta.url)),
          },
          {
            find: /^@\/molecules\//,
            replacement: fileURLToPath(new URL('./src/components/molecules/', import.meta.url)),
          },
          {
            find: /^@\/organisms\//,
            replacement: fileURLToPath(new URL('./src/components/organisms/', import.meta.url)),
          },
          {
            find: '@',
            replacement: fileURLToPath(new URL('./src', import.meta.url)),
          },
        ],
    },
})
