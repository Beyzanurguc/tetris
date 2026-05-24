import sys
from pypdf import PdfReader

try:
    reader = PdfReader("Assignment1_Tetris(edited).pdf")
    text = ""
    for page in reader.pages:
        text += page.extract_text() + "\n--- PAGE BREAK ---\n"
    with open("pdf_output.txt", "w", encoding="utf-8") as f:
        f.write(text)
    print("Done")
except Exception as e:
    print(f"Error: {e}")
