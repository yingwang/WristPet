#!/usr/bin/env python3
"""Generate WristPet launcher icons — pixel art pet on dark background."""
from PIL import Image, ImageDraw
import os

BODY = (76, 175, 80)
EYE_W = (255, 255, 255)
EYE_B = (26, 26, 26)
CHEEK = (255, 138, 128)

def draw_pet_icon(size, bg=None):
    img = Image.new("RGBA", (size, size), bg or (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    cx, cy = size / 2, size / 2
    px = size / 20

    # Body
    for row in range(-4, 4):
        if row in (-4, 3):
            cols = range(-3, 3)
        elif row in (-3, 2):
            cols = range(-4, 4)
        else:
            cols = range(-5, 5)
        for col in cols:
            x = cx + col * px
            y = cy + row * px
            draw.rectangle([x, y, x + px - 0.5, y + px - 0.5], fill=BODY)

    # Eyes
    for ex in [cx - 3 * px, cx + 1 * px]:
        draw.rectangle([ex, cy - 2 * px, ex + 2 * px - 0.5, cy - 0.5], fill=EYE_W)
    draw.rectangle([cx - 2.5 * px, cy - 1.5 * px, cx - 1.5 * px - 0.5, cy - 0.5 * px - 0.5], fill=EYE_B)
    draw.rectangle([cx + 1.5 * px, cy - 1.5 * px, cx + 2.5 * px - 0.5, cy - 0.5 * px - 0.5], fill=EYE_B)

    # Cheeks
    draw.rectangle([cx - 4 * px, cy, cx - 2 * px - 0.5, cy + px - 0.5], fill=CHEEK)
    draw.rectangle([cx + 2 * px, cy, cx + 4 * px - 0.5, cy + px - 0.5], fill=CHEEK)

    # Smile
    draw.rectangle([cx - 2 * px, cy + px, cx - px - 0.5, cy + 2 * px - 0.5], fill=EYE_B)
    draw.rectangle([cx - px, cy + 1.5 * px, cx + px - 0.5, cy + 2.5 * px - 0.5], fill=EYE_B)
    draw.rectangle([cx + px, cy + px, cx + 2 * px - 0.5, cy + 2 * px - 0.5], fill=EYE_B)

    return img

sizes = {
    "hdpi": 72,
    "xhdpi": 96,
    "xxhdpi": 144,
}

base = "app/src/main/res"
for density, sz in sizes.items():
    # Launcher icon: dark bg circle with pet
    launcher = Image.new("RGBA", (sz, sz), (0, 0, 0, 0))
    draw_bg = ImageDraw.Draw(launcher)
    draw_bg.ellipse([0, 0, sz - 1, sz - 1], fill=(30, 30, 50))
    pet = draw_pet_icon(sz)
    launcher = Image.alpha_composite(launcher, pet)

    folder = os.path.join(base, f"mipmap-{density}")
    launcher.save(os.path.join(folder, "ic_launcher.png"))
    launcher.save(os.path.join(folder, "ic_launcher_round.png"))

    # Complication icon: transparent bg, just the pet
    complication = draw_pet_icon(sz)
    os.makedirs(os.path.join(base, f"drawable-{density}"), exist_ok=True)
    complication.save(os.path.join(base, f"drawable-{density}", "ic_complication.png"))

    print(f"Generated {density}: {sz}x{sz}")

print("Done!")
