"use client";

import { useEffect, useState, useRef } from "react";
import Image from "next/image";
import { cn } from "@/lib/utils";

export default function AlternativeSlideshow() {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [currentTextIndex, setCurrentTextIndex] = useState(0);
  const [isTextAnimating, setIsTextAnimating] = useState(false);
  const textTimerRef = useRef(null);
  const imageTimerRef = useRef(null);

  // Using the provided image URLs
  const images = [
    "https://iutoic-dhaka.edu/uploads/img/1705465958_1201.jpg",
    "https://iutoic-dhaka.edu/uploads/img/1705465851_1711.jpg",
    "https://iutoic-dhaka.edu/uploads/img/1600322507_1803.jpg",
    "https://iutoic-dhaka.edu/uploads/img/1553676299.jpg",
  ];

  const texts = ["Welcome to IUT Study Sync", "Best Student environment"];

  const startTextAnimation = () => {
    setIsTextAnimating(true);

    textTimerRef.current = setTimeout(() => {
      setCurrentTextIndex((prevIndex) => (prevIndex + 1) % texts.length);
      setIsTextAnimating(false);

      textTimerRef.current = setTimeout(startTextAnimation, 2000);
    }, 2000);
  };

  useEffect(() => {
    // Start text animation
    startTextAnimation();

    // Image slideshow timer
    imageTimerRef.current = setInterval(() => {
      setCurrentImageIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, 6000);

    return () => {
      if (textTimerRef.current) clearTimeout(textTimerRef.current);
      if (imageTimerRef.current) clearInterval(imageTimerRef.current);
    };
  }, [images.length, texts.length]);

  return (
    <div className="relative h-screen w-full overflow-hidden">
      {/* Image Slideshow */}
      {images.map((src, index) => (
        <div
          key={index}
          className={cn(
            "absolute inset-0 transition-opacity duration-2000",
            currentImageIndex === index ? "opacity-100" : "opacity-0"
          )}
        >
          <div className="relative w-full h-full">
            <Image
              src={src || "/placeholder.svg"}
              alt={`IUT Campus Slide ${index + 1}`}
              fill
              unoptimized
              priority={index === 0}
              className="object-cover"
              onError={(e) => {
                // Fallback to placeholder if image fails to load
                e.target.src = `/placeholder.svg?height=1080&width=1920&text=Image+${
                  index + 1
                }`;
              }}
            />
          </div>
        </div>
      ))}

      {/* Overlay with gradient */}
      <div className="absolute inset-0 bg-gradient-to-b from-black/50 to-black/70" />

      {/* Text Animation */}
      <div className="absolute inset-0 flex flex-col items-center justify-center text-center">
        <div className="relative h-32 overflow-hidden">
          <h1
            className={cn(
              "text-4xl md:text-6xl font-bold text-white transition-all duration-1000",
              isTextAnimating
                ? "opacity-0 transform -translate-y-16"
                : "opacity-100 transform translate-y-0"
            )}
          >
            {texts[currentTextIndex]}
          </h1>
        </div>

        <div className="mt-8">
          <button className="bg-white text-black px-6 py-3 rounded-full font-medium hover:bg-opacity-90 transition-colors">
            Get Started
          </button>
        </div>
      </div>
    </div>
  );
}
