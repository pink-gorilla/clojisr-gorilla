# clojisr-gorilla

PinkGorilla Notebook Visualization Helpers for clojisr.

## Features
- display RObjects as string in notebook
- display R charts in notebook.
- Docstrings for R functions

## Usage

- Adding a dependency to clojisr-gorilla will automatically
add clojisr dependency to your project; no 2 dependencies needed.
- Including the namespace 
  [pinkgorilla.clojisr.repl :refer [pdf-off ->svg r-doc]]
  to your project, will register a renderer for clojisr
  RObject. This typically means RObjects are rendered as text.
- ->svg will show a R Plot in notebook.
- r-doc will print docstring for a R function.

## Demo

```
lein pinkgorilla
```

This will run pinkgorilla notebook as a library, with clojisr demo notebooks loaded into the notebook explorer.