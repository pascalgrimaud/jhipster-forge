/// <reference types="vitest" />

import tsconfigPaths from 'vite-tsconfig-paths';
import { configDefaults, defineConfig } from 'vitest/config';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue(), tsconfigPaths()],
  test: {
    reporters: ['verbose', 'vitest-sonar-reporter'],
    outputFile: {
      'vitest-sonar-reporter': 'target/test-results/TESTS-results-sonar.xml',
    },
    globals: true,
    logHeapUsage: true,
    poolOptions: {
      threads: {
        minThreads: 1,
        maxThreads: 2,
      },
    },
    environment: 'jsdom',
    cache: false,
    include: ['src/test/webapp/unit/**/*.{test,spec}.?(c|m)[jt]s?(x)'],
    coverage: {
      thresholds: {
        perFile: true,
        autoUpdate: true,
        100: true,
      },
      include: ['src/main/webapp/**/*.ts?(x)'],
      exclude: [...(configDefaults.coverage.exclude as string[]), 'src/main/webapp/app/main.ts', 'src/main/webapp/app/injections.ts', 'src/main/webapp/app/router.ts', 'src/main/webapp/**/*.component.ts'],
      provider: 'istanbul',
      reportsDirectory: 'target/test-results/',
      reporter: ['html', 'json-summary', 'text', 'text-summary', 'lcov', 'clover'],
      watermarks: {
        statements: [100, 100],
        branches: [100, 100],
        functions: [100, 100],
        lines: [100, 100],
      },
    },
  },
});
